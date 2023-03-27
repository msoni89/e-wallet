import {Box, Button, Flex, FormControl, FormLabel, Input, Spacer, useToast, VStack} from "@chakra-ui/react";
import React, {useState} from "react";
import ListCurrencies from "./ListCurrencies";
import {CreateWalletRequest, useAddWalletMutation, WalletResponse} from "../../app/services/api";
import {useAuth} from "../../hooks/useAuth";
import {useNavigate} from "react-router-dom";

export const AddWallet = () => {
    const initialValue: CreateWalletRequest = {currencyId: 0, name: ''}
    const [state, setState] = useState(initialValue)
    const [addWallet, {isLoading}] = useAddWalletMutation()
    const auth = useAuth()
    const toast = useToast()

    const handleChange = ({target}: React.ChangeEvent<HTMLInputElement>) => {
        setState((prev) => ({
            ...prev,
            [target.name]: target.value,
        }))
    }
    const handleCurrencyChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
        setState((prev) => ({
            ...prev,
            [target.name]: target.value,
        }))
    }

    const handleClick = () => addWallet({body: state, userId: auth.user!.id}).then((response) => {
            setState({currencyId: 0, name: ''})
        toast({
            status: 'info',
            title: 'Info',
            description: `Yes, account added (${JSON.stringify(response)})!`    ,
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
                        <FormLabel htmlFor="name">Add Wallet</FormLabel>
                        <Input
                            onChange={handleChange}
                            id="name"
                            name="name"
                            placeholder="Enter name"
                        />
                        <ListCurrencies onChange={handleCurrencyChange}></ListCurrencies>
                    </VStack>
                </FormControl>
            </Box>
            <Spacer/>
            <Button
                mt={8}
                colorScheme="purple"
                isLoading={isLoading}
                onClick={handleClick}
            >
                Add Wallet
            </Button>
        </Flex>
    )
}

export default AddWallet