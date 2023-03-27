import {Select} from "@chakra-ui/react";
import {CurrenciesResponse, useGetCurrenciesQuery} from "../../app/services/api";

interface Props {
    onChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
}

const ListCurrencies: React.FC<Props> = ({ onChange }) => {
    const {data, isLoading} = useGetCurrenciesQuery
    ()

    if (isLoading) {
        return <div>Loading</div>
    }

    if (!data) {
        return <div>No currencies :(</div>
    }

    return (
        <Select name={"currencyId"} placeholder='select currency' size='md' onChange={onChange} >
            {data.map(({id, name, symbol, code}: CurrenciesResponse) => (
                <option value={id}>{name} ({code})</option>
            ))}
        </Select>
    )
}
export default ListCurrencies;