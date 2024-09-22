import { appFetch, fetchConfig } from "./appFetch";

export const addComment = (comment, onSuccess, onErrors) =>
    appFetch(
        "/comments/write",
        fetchConfig("POST", comment),
        onSuccess,
        onErrors
    );

export const getTemplateComments = (id, onSuccess, onErrors) =>
    appFetch(
        `/comments/fromTemplate/${id}`,
        fetchConfig("GET"),
        onSuccess,
        onErrors
    );
