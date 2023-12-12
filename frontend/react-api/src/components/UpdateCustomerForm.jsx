import React from 'react';
import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Stack} from "@chakra-ui/react";
import {updateCustomer} from "../services/Client.js";
import {errorNotification, successNotification} from "../services/Notification.js";

const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (<Box>
        <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
        <Input className="text-input" {...field} {...props} />
        {meta.touched && meta.error ? (<Alert className={"error"} status={"error"} mt={0.25} borderRadius={7}>
            <AlertIcon></AlertIcon>
            {meta.error}
        </Alert>) : null}
    </Box>);
};

const UpdateCustomerForm = ({fetchCustomers, initialValues, customerId, onClose}) => {
    return (<>
        <Formik
            initialValues={initialValues}
            validationSchema={Yup.object({
                name: Yup.string()
                    .max(15, 'Must be 15 characters or less')
                    .required('Required'), email: Yup.string()
                    .email('Invalid email address')
                    .required('Required'), age: Yup.number()
                    .min(16, 'Must be at least 16 years of age')
                    .max(100, 'Must be less than 100 years of age')
                    .required('Required'),
            })}

            onSubmit={(updatedCustomer, {setSubmitting}) => {
                setSubmitting(true)
                updateCustomer(customerId, updatedCustomer)
                    .then(res => {
                        console.log(res);
                        fetchCustomers();
                        successNotification("Customer Updated", `${updatedCustomer.name} was successfully updated`)
                        onClose()
                    }).catch(e => {
                    console.log(e);
                    errorNotification(e.code, e.response.data.error)
                })
                    .finally(() => {
                        setSubmitting(false);
                    });
            }}
        >
            {({ isSubmitting, isValid, dirty }) => {
                return (
                    <Form>
                <Stack spacing={"24px"}>
                    <MyTextInput
                        label="Name"
                        name="name"
                        type="text"
                        placeholder="Jane"
                    />

                    <MyTextInput
                        label="Email"
                        name="email"
                        type="email"
                        placeholder="jane@gmail.com"
                    />

                    <MyTextInput
                        label="Age"
                        name="age"
                        type="number"
                        placeholder="24"
                    />

                    <Button disabled={!(isValid && dirty) || isSubmitting}
                            type="submit" mt={2}

                    >
                        Submit
                    </Button>
                </Stack>
            </Form>)}
            }
        </Formik>
    </>);
};

export default UpdateCustomerForm;