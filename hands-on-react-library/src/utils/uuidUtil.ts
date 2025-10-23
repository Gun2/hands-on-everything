export const createUUID = () => {
  const random = Math.random().toString(16).substring(2);
  const timestamp = Date.now().toString(16);
  return `${timestamp}-${random}`; // ex: "17cd48e2f39-1b5f7a2ce"
};
