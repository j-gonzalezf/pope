import Image from 'react-bootstrap/Image';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Navbar from 'react-bootstrap/Navbar';
import { CgGym } from "react-icons/cg";
import './Header.css';

import React, { useCallback, useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useLocation } from 'react-router-dom';

import AnchorIcon from '../../common/images/anchor-logo.webp';
import Dumbbells from '../../common/images/dumbbells.webp';
import TrainerIcon from '../../common/images/trainer-logo.webp';
import templates from '../../templates';
import users from '../../users';

const Header = () => {

    const location = useLocation();
    const dispatch = useDispatch();

    const isLoggedIn = useSelector(users.selectors.isLoggedIn);
    const clientId = useSelector(users.selectors.getClientInfo)?.id;
    const clientName = useSelector(users.selectors.getClientInfo)?.fullName;
    const cycleName = useSelector(templates.selectors.getCycle)?.name;
    const icon = useSelector(users.selectors.getIcon);

    const [showHeader, setShowHeader] = useState(false);
    const [showReference, setShowReference] = useState(false)

    const displayReference = useCallback(() => {

        const urlsToHideCycleName = [
            '/users/clientDetails/' + clientId,
            '/templates/trainingCycles/' + clientId
        ];

        if (clientName && cycleName && !urlsToHideCycleName.includes(location.pathname)) {
            return (
                <>
                    <Link to={`/users/clientDetails/${clientId}`} className='link h'>{clientName}</Link>
                    <span className='required'>/ </span>
                    <Link to={`/templates/trainingCycles/${clientId}`} className='link h'>{cycleName}</Link>
                </>
            );
        } else if (clientName) {
            return <Link to={`/users/clientDetails/${clientId}`} className='link h'>{clientName}</Link>;
        }
        return '';

    }, [clientId, clientName, cycleName, location.pathname]);

    const clearCycle = () => {
        dispatch(templates.actions.clearCycle());
    }

    useEffect(() => {
        setShowHeader(isLoggedIn);
    }, [location, isLoggedIn]);

    useEffect(() => {
        displayReference();
        // URLs to hide the header reference
        const urlsToHide = [
            '/users/clients',
            '/users/addClient',
            '/users/updateProfile',
            '/users/changePassword',
            '/templates/exercises',
        ];
        if (!urlsToHide.includes(location.pathname)) {
            setShowReference(true);
        } else {
            setShowReference(false);
        }
    }, [location.pathname, displayReference]);

    return (

        showHeader ? (

            <Navbar data-testid="header" className="Header">

                <div className="header-left">
                    <Navbar.Brand as={Link} to="/users/clients" title="Clientes" onClick={clearCycle}>
                        <Image className="anchor-icon" src={AnchorIcon} alt="Logo" />
                    </Navbar.Brand>

                    {showReference &&
                        <span>{displayReference()}</span>
                    }

                </div>

                <Link className="dumbbells-link" to="/templates/exercises" title="Lista de ejercicios">
                    <CgGym className="dumbbells-icon" size={55} src={Dumbbells} alt="Lista de ejercicios" />
                </Link>

                <div className="header-right">

                    <Nav className="header-nav">
                        <NavDropdown title={icon !== undefined ?
                            <div className="icon-container">
                                <Image className="icon-image-large" src={"data:image/png;base64," + icon.base64} alt={icon.name} />
                            </div>
                            :
                            <div className="icon-container">
                                <Image className="icon-image-large" src={TrainerIcon} alt="Trainer icon" />
                            </div>
                        }>
                            <NavDropdown.Item as={Link} to="/users/updateProfile">
                                <FormattedMessage id="project.users.viewProfile" />
                            </NavDropdown.Item>
                            <NavDropdown.Item as={Link} to="/users/changePassword">
                                <FormattedMessage id="project.users.changePassword" />
                            </NavDropdown.Item>
                            <NavDropdown.Divider style={{ backgroundColor: '#191716' }} />
                            <NavDropdown.Item as={Link} to="/users/logout">
                                <FormattedMessage id="project.users.logout" />
                            </NavDropdown.Item>
                        </NavDropdown>
                    </Nav>

                </div>

            </Navbar >

        ) : null

    );

}

export default Header;
