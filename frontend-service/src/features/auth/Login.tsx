import * as React from 'react'
import {
    Button,
    Center,
    Divider,
    Heading,
    HStack,
    Input,
    InputGroup,
    InputRightElement,
    useToast,
    VStack
} from '@chakra-ui/react'
import {useNavigate} from 'react-router-dom'
import {useDispatch} from 'react-redux'
import {setCredentials, setRegistrationToken} from './authSlice'

import {LoginRequest, RegisterRequest, useLoginMutation, useRegisterMutation} from '../../app/services/api'

function PasswordInput({
                           name,
                           onChange,
                       }: {
    name: string
    onChange: (event: React.ChangeEvent<HTMLInputElement>) => void
}) {
    const [show, setShow] = React.useState(false)
    const handleClick = () => setShow(!show)

    return (
        <InputGroup size="md">
            <Input
                pr="4.5rem"
                type={show ? 'text' : 'password'}
                placeholder="Enter password"
                name={name}
                onChange={onChange}
            />
            <InputRightElement width="4.5rem">
                <Button h="1.75rem" size="sm" onClick={handleClick}>
                    {show ? 'Hide' : 'Show'}
                </Button>
            </InputRightElement>
        </InputGroup>
    )
}

export const Login = () => {
    const dispatch = useDispatch()
    const navigate = useNavigate()
    const toast = useToast()

    const [loginState, setLoginState] = React.useState<LoginRequest>({
        email: '',
        password: '',
    })

    const [registerState, setRegisterState] = React.useState<RegisterRequest>({
        password: '',
        lastname: '',
        firstname: "",
        email: ""
    })

    const [login, {isLoading: isLoginLoading}] = useLoginMutation()
    const [register, {isLoading: isRegisterLoading}] = useRegisterMutation()

    const handleLoginFormChange = ({
                              target: {name, value},
                          }: React.ChangeEvent<HTMLInputElement>) =>
        setLoginState((prev) => ({...prev, [name]: value}))

    const handleRegisterFormChange = ({
                                       target: {name, value},
                                   }: React.ChangeEvent<HTMLInputElement>) =>
        setRegisterState((prev) => ({...prev, [name]: value}))


    return (
        <Center h="1000px">

            <HStack>
                <Center w="400px">
                    <VStack spacing="4">
                        <Heading>Login</Heading>
                        <InputGroup>
                            <Input
                                onChange={handleLoginFormChange}
                                name="email"
                                type="text"
                                placeholder="Email"
                            />
                        </InputGroup>
                        <InputGroup>
                            <PasswordInput onChange={handleLoginFormChange} name="password"/>
                        </InputGroup>
                        <Button
                            isFullWidth
                            onClick={async () => {
                                try {
                                    const user = await login(loginState).unwrap()
                                    dispatch(setCredentials(user))
                                    navigate('/')
                                } catch (err) {
                                    toast({
                                        status: 'error',
                                        title: 'Error',
                                        description: 'Oh no, there was an error!',
                                        isClosable: true,
                                    })
                                }
                            }}
                            colorScheme="green"
                            isLoading={isLoginLoading}
                        >
                            Login
                        </Button>
                        <Divider/>
                    </VStack>
                </Center>
                <Center w="400px">
                    <VStack spacing="4">
                        <Heading>Register</Heading>
                        <InputGroup>
                            <Input
                                onChange={handleRegisterFormChange}
                                name="firstname"
                                type="text"
                                placeholder="First Name"
                            />
                        </InputGroup>
                        <InputGroup>
                            <Input
                                onChange={handleRegisterFormChange}
                                name="lastname"
                                type="text"
                                placeholder="Last Name"
                            />
                        </InputGroup>
                        <InputGroup>
                            <Input
                                onChange={handleRegisterFormChange}
                                name="email"
                                type="text"
                                placeholder="Email"
                            />
                        </InputGroup>
                        <InputGroup>
                            <PasswordInput onChange={handleRegisterFormChange} name="password"/>
                        </InputGroup>
                        <Button
                            isFullWidth
                            onClick={async () => {
                                try {
                                    const user = await register(registerState).unwrap()
                                    dispatch(setRegistrationToken(user))
                                    toast({
                                        status: 'info',
                                        title: 'Hurray',
                                        description: 'please try to login',
                                        isClosable: true,
                                    })
                                } catch (err) {
                                    toast({
                                        status: 'error',
                                        title: 'Error',
                                        description: 'Oh no, there was an error!',
                                        isClosable: true,
                                    })
                                }
                            }}
                            colorScheme="green"
                            isLoading={isRegisterLoading}
                        >
                            Register
                        </Button>
                        <Divider/>
                    </VStack>
                </Center>
            </HStack>
        </Center>
    )
}

export default Login