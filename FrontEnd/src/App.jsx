import './App.css'

import Tela_admin from './Pages/Tela_admin/Tela_admin'
import Tela_inicial from './Pages/Tela_inicial/Tela_inicial'
import Tela_login from './Pages/Tela_login/Tela_login'
import Tela_registrar from './Pages/Tela_registrar/Tela_registrar'
import {Navbar} from './Components/Navbar/Navbar'
import { Routes, Route } from 'react-router-dom'



function App() {
  return (
    <Routes>
      {/* Tela inicial na raiz */}
      <Route path="/" element={<Tela_inicial />} />
      <Route path="/navbar" element={<Navbar/>}/>
      <Route path="/admin" element={<Tela_admin />} />
      <Route path="/login" element={<Tela_login />} />
      <Route path="/registrar" element={<Tela_registrar />} />

    </Routes>
  )
}

export default App
