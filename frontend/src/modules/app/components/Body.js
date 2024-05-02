import React from "react";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";

import users, { Login } from "../../users";
import Home from "./Home";

const Body = () => {

  const loggedIn = useSelector(users.selectors.isLoggedIn);

  return (

    <div data-testid="body" className="container" >
      <Routes>
        <Route path="/*" element={<Home />} />
        {!loggedIn && <Route path="/users/login" element={<Login />} />}
      </Routes>
    </div >

  );

}

export default Body;
