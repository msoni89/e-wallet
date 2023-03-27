import {Select} from "@chakra-ui/react";
import {useGetUserWalletListQuery, WalletResponse} from "../../app/services/api";
import {useAuth} from "../../hooks/useAuth";

interface Props {
    onChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
    name: string
}

const ListWallets: React.FC<Props> = ({ onChange, name }) => {
    const auth = useAuth()
    const {data, isLoading} = useGetUserWalletListQuery(auth.user!.id);

    if (isLoading) {
        return <div>Loading</div>
    }

    if (!data) {
        return <div>No wallets :(</div>
    }


    return (
        <Select name={name} placeholder='select currency' size='md' onChange={onChange} >
            {data.map(({id, name, accountNumber, balance , currency}: WalletResponse) => (
                <option value={id}>{accountNumber} - {currency.symbol}{balance} </option>
            ))}
        </Select>
    )
}
export default ListWallets;