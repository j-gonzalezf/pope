import { fireEvent, render, screen } from "@testing-library/react";
import React from "react";
import { IntlProvider } from "react-intl";
import { Provider } from 'react-redux';
import { HashRouter } from "react-router-dom";
import renderer from "react-test-renderer";
import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";

import { initReactIntl } from "../../../i18n";
import { Login } from "../../../modules/users";
import { userMock } from "./User.mock";

const { locale, messages } = initReactIntl();

describe("Profile Login", () => {


	it("renders correctly", () => {

	});


});
