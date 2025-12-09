<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Listed-companies</title>
    <link rel="shortcut icon" href="./files/images/favicon-32x32.png" type="image/x-icon">
    <script src="https://kit.fontawesome.com/afcf20c6bc.js" crossorigin="anonymous"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
        }
        header {
            font-size: 28px;
            margin-bottom: 20px;
            font-weight: bold;
            color: #333;
        }
        .card-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
        }
        .card {
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: transform 0.2s ease-in-out;
            width: 300px;
            text-align: center;
        }
        .card:hover {
            transform: translateY(-10px);
        }
        .card img {
            width: 100%;
            height: auto;
            border-bottom: 1px solid #f4f4f4;
            border-radius: 10px 10px 0 0;
        }
        .card h2 {
            margin: 20px 0 10px;
            font-size: 24px;
        }
        .card p {
            color: #777;
            font-size: 16px;
            margin: 0 20px 10px;
        }
        .card a {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
            display: block;
            margin: 10px 0;
        }
        .card a:hover {
            text-decoration: underline;
        }
        .card .email, .card .phone {
            font-size: 14px;
            color: #333;
        }
    </style>
</head>
<body>
    <header>Listed Companies with ProjectPlus</header>
    <div class="card-container">
        <c:forEach items="${allListedCompanies}" var="company">
            <div class="card">
                <img src="./files/webImages/${company.companyProfilePicture}" alt="Company Logo"/>
                <h2>${company.companyName}</h2>
                <a href="${company.companyWebSite}" target="_blank">${company.companyWebSite}</a>
                <p class="email">${company.companyEmail}</p>
                <p class="phone">${company.companyPhone}</p>
                <p class="phone">${company.about}</p>
                <a href="Explorejobs">View Job Opening &nbsp;<i class="fa-solid fa-pen-to-square"></i></a>
            </div>
        </c:forEach>
    </div>
</body>
</html>
