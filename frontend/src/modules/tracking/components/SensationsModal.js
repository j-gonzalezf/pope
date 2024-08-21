import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';

const SensationModal = ({ showModal, setShowModal }) => {

    const dispatch = useDispatch();

    const { clientId } = useParams();
    const { templateId } = useParams();

    const [fatigue, setFatigue] = useState(0);
    const [stiffness, setStiffness] = useState(0);
    const [motivation, setMotivation] = useState(0);
    const [sleep, setSleep] = useState(0);
    const [error, setError] = useState(null);

    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.registerSensations(
                {
                    fatigue: fatigue === 0 ? null : fatigue,
                    stiffness: stiffness === 0 ? null : stiffness,
                    motivation: motivation === 0 ? null : motivation,
                    sleep: sleep === 0 ? null : sleep,
                    sensationDate: new Date().toISOString().slice(0, -1),
                    templateId: Number(templateId),
                    clientId: Number(clientId)
                },
                () => {
                    dispatch(actions.getSensation(templateId,
                        () => { },
                        errors => setError(errors)));
                    closeModal();
                },
                errors => setError(errors)
            ));
        }
    };

    const closeModal = () => {
        setShowModal(false);
        setFatigue(0);
        setStiffness(0);
        setMotivation(0);
        setSleep(0);
        setError(null);
    };

    return (
        <Modal show={showModal} onHide={closeModal} >

            <Modal.Header className="modal-header">
                <Modal.Title>
                    <FormattedMessage id="project.tracking.sensations.title" />
                </Modal.Title>
            </Modal.Header>

            <Modal.Body className="modal-body">

                <Form
                    ref={node => form = node}
                    noValidate
                    onSubmit={handleSubmit}
                >
                    <Form.Group className="mb-3" controlId="fatigue">
                        <Form.Label>
                            <FormattedMessage id="project.tracking.fatigue" />
                        </Form.Label>
                        <Form.Range
                            className='form-range'
                            min={0}
                            max={5}
                            value={fatigue}
                            onChange={e => setFatigue(Number(e.target.value))}
                        />
                        <span>{fatigue === 0 ? '\u00A0' : fatigue}</span>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="stiffness">
                        <Form.Label>
                            <FormattedMessage id="project.tracking.stiffness" />
                        </Form.Label>
                        <Form.Range
                            className='form-range'
                            min={0}
                            max={5}
                            value={stiffness}
                            onChange={e => setStiffness(Number(e.target.value))}
                        />
                        <span>{stiffness === 0 ? '\u00A0' : stiffness}</span>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="motivation">
                        <Form.Label>
                            <FormattedMessage id="project.tracking.motivation" />
                        </Form.Label>
                        <Form.Range
                            className='form-range'
                            min={0}
                            max={5}
                            value={motivation}
                            onChange={e => setMotivation(Number(e.target.value))}
                        />
                        <span>{motivation === 0 ? '\u00A0' : motivation}</span>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="sleep">
                        <Form.Label>
                            <FormattedMessage id="project.tracking.sleep" />
                        </Form.Label>
                        <Form.Range
                            className='form-range'
                            min={0}
                            max={5}
                            value={sleep}
                            onChange={e => setSleep(Number(e.target.value))}
                        />
                        <span>{sleep === 0 ? '\u00A0' : sleep}</span>
                    </Form.Group>

                </Form>

                <Errors errors={error} onClose={() => setError(null)} />

            </Modal.Body>

            <Modal.Footer className="modal-footer">
                <Button variant="secondary" onClick={closeModal}>
                    <FormattedMessage id="project.tracking.sensations.cancel" />
                </Button>
                <Button className="primary cycle" onClick={handleSubmit}>
                    <FormattedMessage id="project.tracking.sensations.button" />
                </Button>
            </Modal.Footer>

        </Modal>
    );

}

export default SensationModal;
