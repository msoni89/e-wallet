import {Box, Button, Flex, FormControl, FormLabel, Input, Spacer, useToast, VStack} from "@chakra-ui/react";
import React, {useState} from "react";
import ListWallets from "./ListWallets";
import {useCreditWalletMutation} from "../../app/services/api";
import {useAuth} from "../../hooks/useAuth";

export const CreditAction = () => {
    const initialValue: { walletId: number, amount: string } = {walletId: 0, amount: '0.00'}
    const [state, setState] = useState(initialValue)
    const [creditWallet, {isLoading}] = useCreditWalletMutation()
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

    const handleClick = () => creditWallet({walletId: state.walletId, amount: state.amount}).then((response) => {
            setState( {walletId: 0, amount: '0.00'})
        toast({
            status: 'info',
            title: 'Info',
            description: 'Yes, account credited!',
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
                        <FormLabel htmlFor="name">Credit Amount</FormLabel>
                        <ListWallets name={"walletId"} onChange={handleSelectChange}></ListWallets>
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
                    Credit
                </Button>
            </Box>
        </Flex>
    )
}

export default CreditAction