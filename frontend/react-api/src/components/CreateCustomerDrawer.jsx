import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const addIcon = () => "+"
const CreateCustomerDrawer = ({fetchCustomers}) => {
    const {isOpen, onOpen, onClose} = useDisclosure()

    return <>
        <Button
            leftIcon={addIcon()}
            colorScheme={"teal"}
            onClick={onOpen}
        >
            Create Customer
        </Button>

        <Drawer isOpen={isOpen} onClose={onClose} size={"lg"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Create new Customer</DrawerHeader>

                <DrawerBody>
                    <CreateCustomerForm
                        onClose={onClose}
                        fetchCustomers={fetchCustomers}
                    />
                </DrawerBody>
            </DrawerContent>
        </Drawer>

    </>
}

export default CreateCustomerDrawer;
