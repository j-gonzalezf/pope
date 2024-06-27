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

export const getCycle = (id, onSuccess, onErrors) =>
    appFetch(
        `/templates/cycle/${id}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );

export const updateCycle = (cycle, onSuccess, onErrors) =>
    appFetch(
        `/templates/${cycle.id}`,
        fetchConfig("PUT", cycle),
        onSuccess,
        onErrors
    );

export const deleteCycle = (id, onSuccess, onErrors) =>
    appFetch(
        `/templates/${id}/delete`, fetchConfig("DELETE"),
        onSuccess,
        onErrors
    );

export const addExercise = (exercise, onSuccess, onErrors) =>
    appFetch(
        "/exercises/exercise/add",
        fetchConfig("POST", exercise),
        onSuccess,
        onErrors
    );

export const getExercises = (trainerId, onSuccess, onErrors) =>
    appFetch(
        `/exercises/${trainerId}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );
