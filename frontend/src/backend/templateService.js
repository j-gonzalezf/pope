import { appFetch, fetchConfig } from "./appFetch";

export const createCycle = (cycle, onSuccess, onErrors) =>
    appFetch(
        "/templates/cycle/create",
        fetchConfig("POST", cycle),
        onSuccess,
        onErrors
    );

export const getCycles = (trainerId, clientId, onSuccess, onErrors) =>
    appFetch(
        `/templates/${trainerId}/clients/${clientId}/cycles`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );
