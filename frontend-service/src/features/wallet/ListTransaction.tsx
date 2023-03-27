import {Box, Flex, Heading, List, ListItem} from "@chakra-ui/react";
import {useAuth} from "../../hooks/useAuth";
import {TransactionResponse, useGetUserTransactionListQuery} from "../../app/services/api";

const ListTransaction = () => {

    const auth = useAuth()
    const {data, isLoading} = useGetUserTransactionListQuery(auth.user!.id);

    if (isLoading) {
        return <div>Loading</div>
    }

    if (!data) {
        return <div>No transactions :(</div>
    }

    return (
        <Box>
            <Flex bg="#011627" p={4} color="white">
                <Box>
                    <Heading size="md">Transactions</Heading>
                </Box>
            </Flex>

            <List spacing={3}>
                {data.map(({ id, transactionType,amount,fromWallet, toWallet, updatedAt, createdAt }: TransactionResponse) => (
                    <ListItem key={id}>
                        from : {fromWallet ? fromWallet?.accountNumber : "Self"} to {toWallet ? toWallet.accountNumber :"Self"}, action {transactionType}, amount {amount}
                    </ListItem>
                ))}
            </List>
        </Box>
    )
}
export default ListTransaction