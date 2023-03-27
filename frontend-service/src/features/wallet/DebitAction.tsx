import {Box, Button, Flex, FormControl, FormLabel, Input, Spacer, useToast, VStack} from "@chakra-ui/react";
import React, {useState} from "react";
import {useDebitWalletMutation} from "../../app/services/api";
import {useAuth} from "../../hooks/useAuth";
import ListWallets from "./ListWallets";

export const DebitAction = () => {
    const initialValue: { walletId: number, amount: string } = {walletId: 0, amount: '0.00'}
    const [state, setState] = useState(initialValue)
    const [debitWallet, {isLoading}] = useDebitWalletMutation()
    const toast = useToast()

    const handleChange = ({target}: React.ChangeEvent<HTMLInputElement>) => {
        setState((prev) => ({
            ...prev,
            [target.name]: target.value,
        }))
    }
    const handleSelectChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
        setState((prev) => ({
            ...prev,
            [target.name]: target.value,
        }))
    }

    const handleClick = () => debitWallet({walletId: state.walletId, amount: state.amount}).then((response) => {
            setState({walletId: 0, amount: '0.00'})
            toast({
                status: 'info',
                title: 'Info',
                description: `Yes, amount debited!`,
                isClosable: true,
            })
        }
    ).catch(() => {
        toast({
            status: 'error',
            title: 'Error',
            description: 'Oh no, there was an error!',
            isClosable: true,
        })
    })

    return (
        <Flex p={5}>
            <Box flex={10}>
                <FormControl>
                    <VStack spacing={4}>
                        <FormLabel htmlFor="name">Debit Amount</FormLabel>
                        <ListWallets name={"walletId"} onChange={handleSelectChange}></ListWallets>
                        <Input
                            id="amount"
                            name="amount"
                            onChange={handleChange}
                            placeholder="debit amount"

                        />
                    </VStack>
                </FormControl>
            </Box>
            <Spacer/>
            <Box>
                <Button
                    mt={8}
                    colorScheme="purple"
                    isLoading={isLoading}
                    onClick={handleClick}
                >
                    Debit
                </Button>
            </Box>
        </Flex>
    )
}

export default DebitAction