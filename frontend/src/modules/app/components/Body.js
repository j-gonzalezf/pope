import React from "react";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";

import users, {
  AddClient, ChangePassword, ClientDetails, ClientsList,
  Login, Logout, SignUp, UpdateClient, UpdateProfile
} from "../../users";
import { } from "../../templates";
import Home from "./Home";

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
        {loggedIn && <Route path="/users/updateClient/:id" element={<UpdateClient />} />}
        {loggedIn && <Route path="/users/clientDetails/:clientId" element={<ClientDetails />} />}
        {/*<Route path="/notFound" element={<NotFoundPage />} />
        /<Route path="/*" element={<Navigate to="/users/addClient" />} />*/}
      </Routes>
    </div >

  );

}

export default Body;
