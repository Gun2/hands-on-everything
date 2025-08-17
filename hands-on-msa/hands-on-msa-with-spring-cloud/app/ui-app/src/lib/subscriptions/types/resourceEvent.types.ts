export type ResourceEvent<T = number,> = {
  id: T,
  eventType: EventType,
}

export type EventType = "CREATED" | "UPDATED" | "DELETED";