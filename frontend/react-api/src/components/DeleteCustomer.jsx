import {
    AlertDialog, AlertDialogBody,
    AlertDialogContent, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button,
    useDisclosure
} from "@chakra-ui/react";
import React from "react";
import {deleteCustomer} from "../services/Client.js";
import {errorNotification, successNotification} from "../services/Notification.js";

const DeleteCustomer = ({name, id, fetchCustomers}) => {
    const cancelRef = React.useRef()
    const {isOpen, onOpen, onClose} = useDisclosure()

    return (
        <>
            <Button
                bg={"red.400"}
                color={"white"}
                rounded={"full"}
                _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                }}
                _focus={{
                    bg: "green.500"
                }}
                onClick={onOpen}
            >
                Delete Customer
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete {name}
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure? You can't undo this action afterwards.
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>

                            <Button colorScheme='red' ml={3} onClick={() =>
                                deleteCustomer(id).then(res => {
                                    console.log(res)
                                    successNotification(
                                        'Customer deleted',
                                        `${name} was successfully deleted.`,
                                        fetchCustomers()
                                    )
                                }).catch(e => {
                                    console.log(e)
                                    errorNotification(e.code, e.response.data.error)
                                }).finally(() =>
                                    onClose()
                                )
                            }>
                                Delete
                            </Button>

                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

export default DeleteCustomer;
