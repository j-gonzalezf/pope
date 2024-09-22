import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

import users from '../../users';

const RedirectHandler = () => {

  const navigate = useNavigate();
  
  const user = useSelector(users.selectors.getUser);

  const path = window.location.hash;

  useEffect(() => {
    if (user.role === 'TRAINER') {
      navigate("/users/clients", { replace: true });
    } else if (user.role === 'CLIENT') {
        const newPath = `/templates/trainingCycles/${user.id}`
      navigate(newPath, { replace: true });
    } else {
      navigate("/notFound", { replace: true });
    }
  }, [path, navigate, user.role, user.id]);

  return null;

};

export default RedirectHandler;
