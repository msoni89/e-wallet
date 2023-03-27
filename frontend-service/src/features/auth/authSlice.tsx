import type {PayloadAction} from '@reduxjs/toolkit'
import {createSlice} from '@reduxjs/toolkit'
import type {User} from '../../app/services/api'
import type {RootState} from '../../app/store'

type AuthState = {
  user: User | null
  token: string | null
  isActive :boolean | null;
}

const slice = createSlice({
  name: 'auth',
  initialState: { user: null, token: null } as AuthState,
  reducers: {
    setCredentials: (
      state,
      { payload: { user, token } }: PayloadAction<{ user: User, token: string }>
    ) => {
      state.user = user
      state.token = token
    },
    setRegistrationToken: (
        state,
        { payload: { token } }: PayloadAction<{ token: string }>
    ) => {
      state.token = token
      state.isActive = false
    },
  },
})

export const { setCredentials, setRegistrationToken } = slice.actions

export default slice.reducer

export const selectCurrentUser = (state: RootState) => state.auth.user