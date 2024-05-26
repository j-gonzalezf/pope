import { appFetch, fetchConfig } from "./appFetch";

export const createCycle = (cycle, onSuccess, onErrors) =>
    appFetch(
        "/templates/cycle/create",
        fetchConfig("POST", cycle),
        onSuccess,
        onErrors
    );
