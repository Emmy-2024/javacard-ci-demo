/**
 * @name Hardcoded PIN in JavaCard application
 * @description Detects a JavaCard field used as a hardcoded PIN.
 * @kind problem
 * @problem.severity warning
 * @security-severity 7.0
 * @precision high
 * @id javacard/hardcoded-pin
 */

import java

from Field f
where
  f.getName().toLowerCase() = "demo_pin"
select f,
  "Hardcoded PIN detected: the PIN is embedded directly in the JavaCard application."