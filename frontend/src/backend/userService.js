import {
  fetchConfig,
  appFetch,
  setServiceToken,
  getServiceToken,
  removeServiceToken,
  setReauthenticationCallback,
} from './appFetch';

const processLoginSignUp = (authenticatedUser, reauthenticationCallback, onSuccess) => {
  setServiceToken(authenticatedUser.serviceToken);
  setReauthenticationCallback(reauthenticationCallback);
  onSuccess(authenticatedUser);
}

export const login = (email, password, onSuccess, onErrors, reauthenticationCallback) =>
  appFetch(
    "/users/login",
    fetchConfig("POST", { email, password }),
    (authenticatedUser) => {
      processLoginSignUp(authenticatedUser, reauthenticationCallback, onSuccess);
    },
    onErrors
  );

export const logout = () => removeServiceToken();

export const tryLoginFromServiceToken = (onSuccess, reauthenticationCallback) => {
  const serviceToken = getServiceToken();

  if (!serviceToken) {
    onSuccess();
    return;
  }

  setReauthenticationCallback(reauthenticationCallback);

  appFetch(
    "/users/loginFromServiceToken",
    fetchConfig("POST"),
    (authenticatedUser) => onSuccess(authenticatedUser),
    () => removeServiceToken()
  );
}

export const signUp = (formData, onSuccess, onErrors, reauthenticationCallback) =>
  appFetch(
    "/users/signUp",
    fetchConfig("POST", formData),
    (authenticatedUser) => {
      processLoginSignUp(authenticatedUser, reauthenticationCallback, onSuccess);
    },
    onErrors
  );

export const addClient = (formData, onSuccess, onErrors) =>
  appFetch(
    "/users/addClient",
    fetchConfig("POST", formData),
    onSuccess,
    onErrors
  );

export const getClients = (id, onSuccess, onErrors) =>
  appFetch(
    `/users/${id}/clients`,
    fetchConfig("GET"),
    onSuccess,
    onErrors
  );

export const getClientInfo = (clientId, onSuccess, onErrors) =>
  appFetch(
    `/users/client/${clientId}`,
    fetchConfig("GET"),
    onSuccess,
    onErrors
  );

export const getTrainerInfo = (trainerId, onSuccess, onErrors) =>
  appFetch(
    `/users/trainer/${trainerId}`,
    fetchConfig("GET"),
    onSuccess,
    onErrors
  );

export const updateProfile = (user, onSuccess, onErrors) =>
  appFetch(
    `/users/${user.id}`,
    fetchConfig("PUT", user),
    onSuccess,
    onErrors
  );

export const changePassword = (id, oldPassword, newPassword, onSuccess, onErrors) =>
  appFetch(
    `/users/${id}/changePassword`,
    fetchConfig("POST", { oldPassword, newPassword }),
    onSuccess,
    onErrors
  );

export const deleteUser = (id, onSuccess, onErrors) =>
  appFetch(
    `/users/${id}/delete`, fetchConfig("DELETE"),
    onSuccess,
    onErrors
  );
