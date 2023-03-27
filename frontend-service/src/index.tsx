import React from 'react'
import ReactDOM from 'react-dom/client'
import {BrowserRouter} from 'react-router-dom'
import {ChakraProvider} from '@chakra-ui/react'
import App from './App'
import {store} from './app/store'
import {Provider} from 'react-redux'


const rootNode = ReactDOM.createRoot(
      document.getElementById('root') as HTMLElement
    )

rootNode.render(
      <React.StrictMode>
        <Provider store={store}>
          <ChakraProvider>
            <BrowserRouter>
              <App />
            </BrowserRouter>
          </ChakraProvider>
        </Provider>
      </React.StrictMode>
    )
