import { appFetch, fetchConfig } from "./appFetch";

export const sensationsRegister = (sensations, onSuccess, onErrors) =>
    appFetch(
        "/sensations/create",
        fetchConfig("POST", sensations),
        onSuccess,
        onErrors
    );

export const getSensation = (templateId, onSuccess, onErrors) =>
    appFetch(
        `/sensations/${templateId}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );

export const updateSensation = (sensation, onSuccess, onErrors) =>
    appFetch(
        `/sensations/${sensation.id}/update`,
        fetchConfig("PUT", sensation),
        onSuccess,
        onErrors
    );
