import { appFetch, fetchConfig } from "./appFetch";

export const addComment = (comment, onSuccess, onErrors) =>
    appFetch(
        "/comments/write",
        fetchConfig("POST", comment),
        onSuccess,
        onErrors
    );
