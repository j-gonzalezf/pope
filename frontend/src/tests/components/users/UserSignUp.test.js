import { fireEvent, render, screen } from "@testing-library/react";
import React from "react";
import { IntlProvider } from "react-intl";
import { Provider } from 'react-redux';
import { HashRouter } from "react-router-dom";
import renderer from "react-test-renderer";
import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";

import { initReactIntl } from "../../../i18n";
import { SignUp } from "../../../modules/users";
import { userMock } from "./User.mock";

const { locale, messages } = initReactIntl();

describe("Profile Sign Up", () => {
/*
	const middlewares = [thunk];
	const mockStore = configureMockStore(middlewares);
	let store;
	
    store = mockStore(userMock);
*/	
    it("renders correctly", () => {
        /*
		const tree = renderer.create(
			<Provider store={store}>
				<IntlProvider locale={locale} messages={messages}>
					<HashRouter>
						<SignUp />
					</HashRouter>
				</IntlProvider>
			</Provider>

		).toJSON();
		expect(tree).toMatchSnapshot();
        */
	});
/*
	it("Get UpdateProfile components ", () => {
		render(
			<Provider store={store}>
				<IntlProvider locale={locale} messages={messages}>
					<HashRouter>
						<SignUp />
					</HashRouter>
				</IntlProvider>
			</Provider>
		)

		const email = screen.getByTestId('email');
		const password = screen.getByTestId('password');
		const confirmPassword = screen.getByTestId('confirmPassword');
		const fullName = screen.getByTestId('fullName');
		const phone = screen.getByTestId('phone');
		const icon = screen.getByTestId('icon');
        const socialLinks = screen.getByTestId('socialLinks');
		const submit = screen.getByTestId('submit');

        fireEvent.input(fullName)
		fireEvent.input(email)
		fireEvent.input(confirmPassword)
		fireEvent.input(password)
		fireEvent.input(phone)
		fireEvent.input(icon)
        fireEvent.input(socialLinks)
		fireEvent.submit(submit)

	});
*/    
});
