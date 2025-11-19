import './App.css'

import Tela_inicial from './Pages/Tela_inicial/Tela_inicial'
import {Navbar} from './Components/Navbar/Navbar'
import { Routes, Route } from 'react-router-dom'

function App() {
  return (
    <Routes>
      {/* Tela inicial na raiz */}
      <Route path="/" element={<Tela_inicial />} />
      <Route path="/navbar" element={<Navbar/>}/>
    </Routes>
  )
}

export default App
