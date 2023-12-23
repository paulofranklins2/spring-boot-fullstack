import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack} from "@chakra-ui/react";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";

const Signup = ({onSuccess}) => {
    const {customer, setCustomerFromToken} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (customer) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        src='/src/resources/image/paulofranklins-logos_black.png'
                        alt='PAULOFRANKLINS'
                        boxSize={"200px"}
                        alignSelf={"center"}
                    />
                    <Heading fontSize={'2xl'} mb={15}>Create account</Heading>
                    <CreateCustomerForm onSuccess={(token) => {
                        localStorage.setItem("access_token", token);
                        setCustomerFromToken();
                        navigate("/dashboard");
                    }}/>
                    <Link color={"blue.500"} href={"/"}>
                        Already have a account? Sign in.
                    </Link>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                p={10}
                flexDirection={"column"}
                alignItems={"center"}
                justifyContent={"center"}
                bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}
            >
                <Image
                    objectFit={'scale-down'}
                    src='/src/resources/image/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png'
                    alt='PAULOFRANKLINS'
                />
            </Flex>
        </Stack>
    );
}

export default Signup;