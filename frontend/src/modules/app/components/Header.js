import Image from 'react-bootstrap/Image';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Navbar from 'react-bootstrap/Navbar';
import './Header.css';

import React, { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useSelector } from 'react-redux';
import { Link, useLocation } from 'react-router-dom';

import AnchorIcon from '../../common/images/anchor-logo.webp';
import TrainerIcon from '../../common/images/trainer-logo.webp';
import users from '../../users';

const Header = () => {

    const location = useLocation();

    const isLoggedIn = useSelector(users.selectors.isLoggedIn);
    const icon = useSelector(users.selectors.getIcon);

    const [showHeader, setShowHeader] = useState(false);

    useEffect(() => {
        setShowHeader(isLoggedIn);
    }, [location, isLoggedIn]);

    return (

        showHeader ? (

            <Navbar data-testid="header" className="Header">

                <Navbar.Brand as={Link} to="/users/clients">
                    <Image className="anchor-icon" src={AnchorIcon} alt="Logo" />
                </Navbar.Brand>

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
