import React from "react";
import {Text} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";


const Home = () => {
    return (
        <SidebarWithHeader>
            <Text fontSize={'6xl'}>Home</Text>
        </SidebarWithHeader>
    )
}

export default Home;