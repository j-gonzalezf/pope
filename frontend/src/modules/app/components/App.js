import "./App.css";

import { useEffect } from "react";
import { useDispatch } from "react-redux";

import users from "../../users";
import Body from "./Body";

const App = () => {

  const dispatch = useDispatch();

  useEffect(() => {

    dispatch(users.actions.tryLoginFromServiceToken(
      () => dispatch(users.actions.logout())));

  })

  return (
    <div>
      <Body />
    </div>
  );

}

export default App;
