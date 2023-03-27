import {Route, Routes} from 'react-router-dom'
import {Box, Divider, Flex, Heading, Spacer} from '@chakra-ui/react'

import {Login} from './features/auth/Login'
import {PrivateOutlet} from './utils/PrivateOutlet'
import {useAuth} from "./hooks/useAuth";
import AddWallet from "./features/wallet/AddWallet";
import CreditAction from "./features/wallet/CreditAction";
import DebitAction from "./features/wallet/DebitAction";
import TransferAction from "./features/wallet/TransferAction";
import ListTransaction from "./features/wallet/ListTransaction";

function MainPage() {

    const auth = useAuth()
    return (
        <Box>
            <Flex bg="#011627" p={4} color="white">
                <Box>
                    <Heading size="xl">e-wallet</Heading>
                </Box>
                <Spacer />
                <Box>
                    <Heading size="xl">{auth.user!.firstname} {auth.user!.lastname}</Heading>
                </Box>
            </Flex>
            <Divider />
            <Divider />
            <Flex wrap="wrap">
                <Box flex={2} borderRight="1px solid #eee">
                    <Box p={4}>
                        <AddWallet />
                    </Box>
                    <Box p={4}>
                        <CreditAction />
                    </Box>
                </Box>
                <Box flex={2}>
                    <Box p={4}>
                        <DebitAction />
                    </Box>
                    <Box p={4}>
                        <TransferAction />
                    </Box>
                </Box>
            </Flex>
            <Flex wrap="wrap">
                <Box flex={2}>
                    <ListTransaction />
                </Box>
            </Flex>
        </Box>
    )
}

function App() {
    return (
        <Box>
            <Routes>
                <Route path="/login" element={<Login/>}/>
                <Route path="*" element={<PrivateOutlet/>}>
                    <Route index element={<MainPage/>}/>
                </Route>
            </Routes>
        </Box>
    )
}

export default App
