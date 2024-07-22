import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';

import { FormattedMessage } from 'react-intl';

const NotFoundPage = () => (
    <Container>
        <Row>
            <Col>
                <br />
                <h1 className="text-white">
                    Error 404 - <FormattedMessage id="project.common.notFoundPage" />
                </h1>
                <p className="text-white">
                    <FormattedMessage id="project.common.notFoundPageMessage" />
                </p>
            </Col>
        </Row>
    </Container>
);

export default NotFoundPage;
