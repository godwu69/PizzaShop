import {User} from './user';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  code: number;
  message: string;
  data: {
    "access_token": string;
    "refresh_token": string;
    "refresh_expires_in": number,
    "expires_in": number
  };
  token: string;
}

export interface RegisterRequest {
  username: string;
  name: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  code: string;
  message: string;
}
