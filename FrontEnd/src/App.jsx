import './App.css'

import Tela_admin from './Pages/Tela_admin/Tela_admin'
import Tela_inicial from './Pages/Tela_inicial/Tela_inicial'
import {Navbar} from './Components/Navbar/Navbar'
import { Routes, Route } from 'react-router-dom'

function App() {
  return (
    <Routes>
      {/* Tela inicial na raiz */}
      <Route path="/" element={<Tela_inicial />} />
      <Route path="/navbar" element={<Navbar/>}/>
      <Route path="/admin" element={<Tela_admin />} />

    </Routes>
  )
}

export default App
