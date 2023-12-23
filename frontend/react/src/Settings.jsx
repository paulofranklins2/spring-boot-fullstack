import React from "react";
import {Text} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";

const Settings = () => {
    return (
        <SidebarWithHeader>
            <Text fontSize={'6xl'}>Settings</Text>
        </SidebarWithHeader>
    );
};

export default Settings;
