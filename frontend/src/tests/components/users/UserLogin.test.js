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

	/*
	const middlewares = [thunk];
	const mockStore = configureMockStore(middlewares);
	let store= mockStore(userMock);
	*/

	it("renders correctly", () => {
		/*
		const tree = renderer.create(
			<Provider store={store}>
				<IntlProvider locale={locale} messages={messages}>
					<HashRouter>
						<Login />
					</HashRouter>
				</IntlProvider>
			</Provider>
		).toJSON();
		expect(tree).toMatchSnapshot();
		*/
	});

	/*
	it("Get Login components", () => {
		render(
			<Provider store={store}>
				<IntlProvider locale={locale} messages={messages}>
					<HashRouter>
						<Login />
					</HashRouter>
				</IntlProvider>
			</Provider>
		)

		const userName = screen.getByTestId('email');
		const password = screen.getByTestId('password');
		const submit = screen.getByTestId('submit');

		fireEvent.input(userName)
		fireEvent.input(password)
		fireEvent.submit(submit)

	});
	*/

});
