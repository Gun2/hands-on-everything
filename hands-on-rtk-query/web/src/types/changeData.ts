
export type ChangeDataEvent<T> = {
  type: Type;
  data: T;
}

type Type = "CREATE" | "UPDATE" | "DELETE"