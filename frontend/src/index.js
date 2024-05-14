import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';

import React from 'react';
import ReactDOM from 'react-dom/client';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import { HashRouter } from 'react-router-dom';

import store from '../src/store';
import backend, { NetworkError } from './backend';
import { initReactIntl } from './i18n';
import app, { App } from './modules/app/';

backend.init(error => store.dispatch(app.actions.error(new NetworkError())));

const { locale, messages } = initReactIntl();

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <React.StrictMode>
        <Provider store={store}>
            <IntlProvider locale={locale} messages={messages}>
                <HashRouter>
                    <App />
                </HashRouter>
            </IntlProvider>
        </Provider>
    </React.StrictMode>
);
