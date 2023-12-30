import {CustomerDTO} from "./customerDTO";

export interface AuthResponse {
    token?: string
    customerDTO: CustomerDTO;
}
