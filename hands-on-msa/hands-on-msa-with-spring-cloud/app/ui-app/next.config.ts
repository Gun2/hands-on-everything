import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  rewrites : async () => [
    {
      source: "/service/:path*",
      destination: `${process.env.API_BASE_URL}/:path*`
    }
  ],
  logging: {
    fetches: {
      fullUrl: true,
    },
  },
};

export default nextConfig;
