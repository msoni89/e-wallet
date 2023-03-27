import {Box, Button, Flex, FormControl, FormLabel, Input, Spacer, useToast, VStack} from "@chakra-ui/react";
import React, {useState} from "react";
import ListWallets from "./ListWallets";
import {useDebitWalletMutation, useTransferMutation} from "../../app/services/api";

export const TransferAction = () => {
    const toast = useToast()

    const initialValue: { fromWalletId: number, toWalletId: number, amount: string } = {
        toWalletId: -1,
        fromWalletId: -1,
        amount: '0.00'
    }
    const [state, setState] = useState(initialValue)
    const [transfer, {isLoading}] = useTransferMutation()

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

    const handleClick = () => transfer({
        fromWalletId: state.fromWalletId,
        toWalletId: state.toWalletId,
        amount: state.amount
    }).then((response) => {
            setState({toWalletId: -1, fromWalletId: -1, amount: '0.00'})
            toast({
                status: 'info',
                title: 'Info',
                description: 'Yes, amount transfered!',
                isClosable: true,
            })
        }

    ).catch(()=>{
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
                        <FormLabel htmlFor="name">Transfer Amount</FormLabel>
                        <ListWallets name={"fromWalletId"} onChange={handleSelectChange}></ListWallets>
                        <ListWallets name={"toWalletId"} onChange={handleSelectChange}></ListWallets>
                        <Input
                            id="amount"
                            name="amount"
                            onChange={handleChange}
                            placeholder="Credit amount"

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
                    Transfer
                </Button>
            </Box>
        </Flex>
    )
}

export default TransferAction