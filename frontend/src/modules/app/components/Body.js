import React from "react";
import { useSelector } from "react-redux";
import { Navigate, Route, Routes } from "react-router-dom";

import users, {
  AddClient, ChangePassword, ClientDetails, ClientsList,
  Login, Logout, SignUp, UpdateClient, UpdateProfile
} from "../../users";
import { CyclesList, ExercisesList, TemplatesList, TemplateView } from "../../templates";
import Home from "./Home";
import NotFoundPage from "./NotFoundPage";

const Body = () => {

  const loggedIn = useSelector(users.selectors.isLoggedIn);

  return (

    <div data-testid="body" className="container" >
      <Routes>
        {!loggedIn && <Route path="/" element={<Home />} />}
        {!loggedIn && <Route path="/users/login/:userType" element={<Login />} />}
        {!loggedIn && <Route path="/users/signUp" element={<SignUp />} />}
        {loggedIn && <Route path="/users/logout" element={<Logout />} />}
        {loggedIn && <Route path="/users/clients" element={<ClientsList />} />}
        {loggedIn && <Route path="/users/addClient" element={<AddClient />} />}
        {loggedIn && <Route path="/users/updateProfile" element={<UpdateProfile />} />}
        {loggedIn && <Route path="/users/changePassword" element={<ChangePassword />} />}
        {loggedIn && <Route path="/users/updateClient/:clientId" element={<UpdateClient />} />}
        {loggedIn && <Route path="/users/clientDetails/:clientId" element={<ClientDetails />} />}
        {loggedIn && <Route path="/templates/trainingCycles/:clientId" element={<CyclesList />} />}
        {loggedIn && <Route path="/templates/:clientId/trainingCycle/:cycleId" element={<TemplatesList />} />}
        {loggedIn && <Route path="/templates/:clientId/trainingCycle/:cycleId/template/:templateId" element={<TemplateView />} />}
        {loggedIn && <Route path="/templates/exercises" element={<ExercisesList />} />}
        <Route path="/notFound" element={<NotFoundPage />} />
        <Route path="/*" element={<Navigate to="/notFound" />} />
      </Routes>
    </div >

  );

}

export default Body;
