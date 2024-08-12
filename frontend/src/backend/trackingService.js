import { appFetch, fetchConfig } from "./appFetch";

export const sensationsRegister = (sensations, onSuccess, onErrors) =>
    appFetch(
        "/sensations/create",
        fetchConfig("POST", sensations),
        onSuccess,
        onErrors
    );
