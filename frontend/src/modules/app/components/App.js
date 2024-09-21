import "./App.css";

import { useEffect } from "react";
import { useDispatch } from "react-redux";

import users from "../../users";
import Header from "./Header";
import Body from "./Body";

const App = () => {

  const dispatch = useDispatch();

  useEffect(() => {

    dispatch(users.actions.tryLoginFromServiceToken(
      () => dispatch(users.actions.logout())));

  }, [dispatch]);

  return (
    <div>
      <Header />
      <Body />
    </div>
  );

}

export default App;
