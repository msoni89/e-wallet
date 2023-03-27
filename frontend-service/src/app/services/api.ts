import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {RootState} from '../store'


export interface TransactionResponse {

    id: number

    createdAt: Date
    updatedAt: Date
    amount: string

    transactionType: string

    fromWallet: WalletResponse

    toWallet: WalletResponse
}

export interface User {
    email: string
    firstname: string
    id: number
    lastname: string

}

export interface UserResponse {
    user: User
    token: string
}

export interface LoginRequest {
    email: string
    password: string
}

export interface CurrenciesResponse {
    id: number
    code: string

    name: string
    symbol: string
}


export interface WalletResponse {
    id: number
    name: string
    accountNumber: string
    currency: CurrenciesResponse
    balance: number

    enabled: boolean

}

export interface CreateWalletRequest {
    name: string
    currencyId: number
}

export interface RegisterRequest {
    firstname: string

    lastname: string

    email: string
    password: string
}

export const api = createApi({
    baseQuery: fetchBaseQuery({
        baseUrl: process.env.REACT_APP_API_BASE_URL,
        prepareHeaders: (headers, {getState}) => {
            const token = (getState() as RootState).auth.token
            // By default, if we have a token in the store, let's use that for authenticated requests
            if (token) {
                headers.set('authorization', `Bearer ${token}`)
            }
            return headers
        },
    }),
    tagTypes: ['Currencies', "Wallet", "Wallets", "Transactions"],
    endpoints: (builder) => ({
        login: builder.mutation<UserResponse, LoginRequest>({
            query: (credentials) => ({
                url: '/api/v1/auth/signin',
                method: 'POST',
                body: credentials,
            }),
        }),
        register: builder.mutation<UserResponse, LoginRequest>({
            query: (credentials) => ({
                url: '/api/v1/auth/signup',
                method: 'POST',
                body: credentials,
            }),
        }),
        addWallet: builder.mutation<WalletResponse, { body: CreateWalletRequest, userId: number }>({
            query: ({body, userId}) => ({
                url: `/api/v1/wallet/${userId}`,
                method: 'POST',
                body,
            }),
            invalidatesTags: ["Wallets"],
        }),
        creditWallet: builder.mutation<void, { walletId: number, amount: string }>({
            query: ({walletId, amount}) => ({
                url: `/api/v1/wallet/${walletId}/credit?amount=${amount}`,
                method: 'POST',
            }),
            invalidatesTags: ["Wallets", "Transactions"],
        }),
        debitWallet: builder.mutation<void, { walletId: number, amount: string }>({
            query: ({walletId, amount}) => ({
                url: `/api/v1/wallet/${walletId}/debit?amount=${amount}`,
                method: 'POST',
            }),
            invalidatesTags: ["Wallets", "Transactions"],
        }),
        transfer: builder.mutation<void, { fromWalletId: number, toWalletId: number, amount: string }>({
            query: ({fromWalletId, toWalletId, amount}) => ({
                url: `/api/v1/wallet/transfer/${fromWalletId}/${toWalletId}?amount=${amount}`,
                method: 'POST',
            }),
            invalidatesTags: ["Wallets", "Transactions"],
        }),
        getCurrencies: builder.query<CurrenciesResponse[], void>({
            query: () => "/api/v1/currency",
            providesTags: (result) =>
                result
                    ? [
                        ...result.map(({id}) => ({type: 'Currencies' as const, id})),
                        "Currencies",
                    ]
                    : ["Currencies"],
        }),
        getUserWalletList: builder.query<WalletResponse[], number>({
            query: (id) => `/api/v1/wallet/${id}/list`,
            providesTags: (result) =>
                result
                    ? [
                        ...result.map(({id}) => ({type: 'Currencies' as const, id})),
                        "Wallets",
                    ]
                    : ["Wallets"],
        }),
        getUserTransactionList: builder.query<TransactionResponse[], number>({
            query: (id) => `/api/v1/transaction/${id}/list`,
            providesTags: (result) =>
                result
                    ? [
                        ...result.map(({id}) => ({type: 'Currencies' as const, id})),
                        "Transactions",
                    ]
                    : ["Transactions"],
        }),
    }),
})

export const {
    useLoginMutation,
    useRegisterMutation,
    useAddWalletMutation,
    useGetCurrenciesQuery,
    useGetUserWalletListQuery,
    useGetUserTransactionListQuery,
    useCreditWalletMutation,
    useDebitWalletMutation,
    useTransferMutation,
} = api