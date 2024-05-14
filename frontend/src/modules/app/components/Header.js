import Container from 'react-bootstrap/Container';
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
    const email = useSelector(users.selectors.getEmail);
    const icon = useSelector(users.selectors.getIcon);

    const [showHeader, setShowHeader] = useState(false);

    // No mostrar el Header en las siguientes rutas o si el usuario no está autenticado
    const hiddenRoutes = ['/pope', '/users/addClient'];

    useEffect(() => {
        setShowHeader(!hiddenRoutes.includes(location.pathname) && isLoggedIn);
    }, [location, isLoggedIn]);

    return (

        showHeader ? (

            <Navbar data-testid="header" expand="lg" variant="light" className="border">

                <Container className="icon-container">
                    <Navbar.Brand as={Link} to="/users/clients">
                        <Image className="anchor-icon" src={AnchorIcon} alt="Logo" />
                    </Navbar.Brand>
                </Container>

                <Navbar.Toggle aria-controls="navbarSupportedContent" />

                <Navbar.Collapse className="collapse navbar-collapse" id="navbarSupportedContent" >

                    <Nav>
                        <NavDropdown title={icon !== undefined ?
                            <div className="icon-container">
                                <Image className="icon-image" src={"data:image/png;base64," + icon.base64} alt={icon.name} />
                                {email}
                            </div>
                            :
                            <div className="icon-container">
                                <Image className="icon-image" src={TrainerIcon} width="16" height="16" fill="currentColor" viewBox="0 0 16 16" />
                            </div>
                        }>
                            <NavDropdown.Item as={Link} to="/users/updateProfile">
                                <FormattedMessage id="project.users.viewProfile" />
                            </NavDropdown.Item>
                            <NavDropdown.Item as={Link} to="/users/changePassword">
                                <FormattedMessage id="project.users.changePassword" />
                            </NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item as={Link} to="/users/logout">
                                <FormattedMessage id="project.users.logout" />
                            </NavDropdown.Item>
                        </NavDropdown>
                    </Nav>

                </Navbar.Collapse >

            </Navbar >

        ) : null

    );

}

export default Header;
