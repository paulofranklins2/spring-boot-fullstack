import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
    Button,
    AlertDialogFooter,
    AlertDialogBody,
    AlertDialogHeader,
    AlertDialogOverlay,
    AlertDialogContent,
    AlertDialog,
    useDisclosure,
} from '@chakra-ui/react';
import {deleteCustomer} from "../services/Client.js";
import React from "react";
import {errorNotification, successNotification} from "../services/Notification.js";
import UpdateCustomerDrawer from "./UpdateCustomerDrawer.jsx";

export default function CardWithImage({id, name, email, age, gender, imageNumber, fetchCustomers}) {
    const defineGender = gender === 'MALE' ? 'men' : 'women';
    const profilePicture = `https://randomuser.me/api/portraits/${defineGender}/${imageNumber}.jpg`;
    const cancelRef = React.useRef()
    const {isOpen, onOpen, onClose} = useDisclosure()

    return (<Center py={6}>
        <Box
            maxW={'300px'}
            minW={'300px'}
            w={'full'}
            bg={useColorModeValue('white', 'gray.800')}
            boxShadow={'lg'}
            rounded={'md'}
            overflow={'hidden'}>
            <Image
                h={'120px'}
                w={'full'}
                src={'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'}
                objectFit={'cover'}
            />
            <Flex justify={'center'} mt={-12}>
                <Avatar
                    size={'xl'}
                    src={profilePicture}
                    alt={'Author'}
                    css={{
                        border: '2px solid white',
                    }}
                />
            </Flex>

            <Box p={6}>
                <Stack spacing={2} align={'center'} mb={5}>
                    <Tag borderRadius={"full"}>{id}</Tag>
                    <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                        {name}
                    </Heading>
                    <Text color={'gray.500'}>{email}</Text>
                    <Text color={'gray.500'}>Age {age} | Gender: {gender}</Text>
                </Stack>
            </Box>

            <Stack p={2} direciton={'row'} justify={'center'} spacing={6}>
                <Stack>
                    <UpdateCustomerDrawer
                        fetchCustomers={fetchCustomers}
                        initialValues={{name, email, age}}
                        customerId={id}
                    />

                </Stack>

                <Stack>
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
                </Stack>

            </Stack>

        </Box>
    </Center>)
        ;
}