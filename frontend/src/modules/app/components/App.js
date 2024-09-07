import "./App.css";

import { useEffect } from "react";
import { useDispatch } from "react-redux";

import users from "../../users";
import Header from "./Header";
import Body from "./Body";
import SuccessMessage from '../../common/components/SuccessMessage';

const App = () => {

  const dispatch = useDispatch();

  useEffect(() => {

    dispatch(users.actions.tryLoginFromServiceToken(
      () => dispatch(users.actions.logout())));

  })

  return (
    <div>
      <Header />
      <Body />
      <SuccessMessage />
    </div>
  );

}

export default App;
