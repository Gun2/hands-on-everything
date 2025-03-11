
export type LoginRequest = {
  username: string;
  password: string;
}

export type TokenResponse = {
  accessToken: string;
  accessTokenExpiresIn: number;
  refreshTokenExpiresIn: number;
  tokenType: string;
  refreshToken: string;
}