
export type LoginRequest = {
  username: string;
  password: string;
}

export type TokenResponse = {
  accessToken: string;
  accessTokenExpiresIn: number;
  accessTokenExpiresAt: number;
  refreshTokenExpiresIn: number;
  tokenType: string;
  refreshToken: string;
}

export type LoginSuccessResponse = {
  session: string
}
