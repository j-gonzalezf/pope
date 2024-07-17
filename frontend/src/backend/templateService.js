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

export const getExercise = (id, onSuccess, onErrors) =>
    appFetch(
        `/exercises/exercise/${id}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );

export const updateExercise = (exercise, onSuccess, onErrors) =>
    appFetch(
        `/exercises/${exercise.id}`,
        fetchConfig("PUT", exercise),
        onSuccess,
        onErrors
    );

export const deleteExercise = (id, onSuccess, onErrors) =>
    appFetch(
        `/exercises/${id}/delete`,
        fetchConfig("DELETE"),
        onSuccess,
        onErrors
    );

export const createTemplate = (template, onSuccess, onErrors) =>
    appFetch(
        "/templates/create",
        fetchConfig("POST", template),
        onSuccess,
        onErrors
    );

export const getTemplates = (cycleId, onSuccess, onErrors) =>
    appFetch(
        `/templates/fromCycle/${cycleId}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );

export const getTemplate = (id, onSuccess, onErrors) =>
    appFetch(
        `/templates/template/${id}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );

export const addTemplateRow = (row, onSuccess, onErrors) =>
    appFetch(
        "/templates/addRow",
        fetchConfig("POST", row),
        onSuccess,
        onErrors
    );

export const getTemplateRows = (templateId, onSuccess, onErrors) =>
    appFetch(
        `/templates/${templateId}/rows`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );
